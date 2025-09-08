resource "aws_vpc" "vpc" {
  cidr_block           = var.vpc_cidr
  enable_dns_hostnames = true
  enable_dns_support   = true
  tags = {
    Name = "${local.prefix} VPC"
  }
}
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.vpc.id
  tags = {
    Name = "${local.prefix} VPC IGW"
  }
}
resource "aws_subnet" "public_subnets" {
  vpc_id                  = aws_vpc.vpc.id
  for_each                = var.public_subnets
  cidr_block              = each.value.cidr
  map_public_ip_on_launch = true
  availability_zone       = each.value.az
  tags = {
    Name = "${local.prefix} Public ${title(each.key)}"
  }
  depends_on = [aws_internet_gateway.igw]
}

resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }
  tags = {
    Name = "${local.prefix} Public RT"
  }
}

resource "aws_route_table_association" "public_route_table_association" {
  for_each       = aws_subnet.public_subnets
  subnet_id      = each.value.id
  route_table_id = aws_route_table.public_route_table.id
}


resource "aws_security_group" "server_sg" {
  vpc_id      = aws_vpc.vpc.id
  name        = "${local.prefix} Server SG"
  description = "Security Group for Application Server"
  ingress {
    description     = "Allow Traffic HTTP from Load Balancer"
    from_port       = 80
    to_port         = 80
    protocol        = "tcp"
    security_groups = ["0.0.0.0/0"]
  }
  ingress {
    description     = "Allow Traffic HTTPS from Load Balancer"
    from_port       = 443
    to_port         = 443
    protocol        = "tcp"
    security_groups = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow SSH traffic from EC2 Connect Endpoint"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }
  tags = {
    Name = "${local.prefix} Server SG"
  }
}
