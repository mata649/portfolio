resource "aws_vpc" "vpc" {
  cidr_block           = var.vpc_cidr_block
  enable_dns_support   = true
  enable_dns_hostnames = true
  tags = {
    Name = "${local.prefix} VPC"
  }
}

resource "aws_subnet" "public_subnets" {
  for_each                = var.public_subnets
  vpc_id                  = aws_vpc.vpc.id
  cidr_block              = each.value.cidr
  map_public_ip_on_launch = true
  availability_zone       = each.value.az
  tags = {
    Name = "${local.prefix} Public ${title(each.key)}"
  }
  depends_on = [aws_internet_gateway.igw]
}

resource "aws_subnet" "private_subnets" {
  for_each                = var.private_subnets
  vpc_id                  = aws_vpc.vpc.id
  cidr_block              = each.value.cidr
  map_public_ip_on_launch = true
  availability_zone       = each.value.az
  tags = {
    Name = "${local.prefix} Private ${title(each.key)}"
  }
  depends_on = [aws_internet_gateway.igw]
}


resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.vpc.id
  tags = {
    Name = "${local.prefix} VPC IGW"
  }
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

resource "aws_security_group" "api_sg" {
  description = "Allow HTTPS and SSH traffic"
  vpc_id      = aws_vpc.vpc.id
  tags = {
    Name = "${local.prefix} API SG"
  }
}

resource "aws_vpc_security_group_ingress_rule" "allow_https" {
  ip_protocol       = "tcp"
  cidr_ipv4         = "0.0.0.0/0"
  from_port         = 443
  to_port           = 443
  security_group_id = aws_security_group.api_sg.id
}
resource "aws_vpc_security_group_ingress_rule" "allow_http" {
  ip_protocol       = "tcp"
  cidr_ipv4         = "0.0.0.0/0"
  from_port         = 80
  to_port           = 80
  security_group_id = aws_security_group.api_sg.id
}

resource "aws_vpc_security_group_ingress_rule" "allow_ssh" {
  ip_protocol       = "tcp"
  cidr_ipv4         = "0.0.0.0/0"
  from_port         = 22
  to_port           = 22
  security_group_id = aws_security_group.api_sg.id
}
resource "aws_vpc_security_group_egress_rule" "allow_all_egress_traffic" {
  ip_protocol       = "-1"
  cidr_ipv4         = "0.0.0.0/0"
  security_group_id = aws_security_group.api_sg.id
}

resource "aws_security_group" "db_sg" {
  description = "Allow Traffic from API"
  vpc_id      = aws_vpc.vpc.id
  tags = {
    Name = "${local.prefix} DB SG"
  }
}
resource "aws_vpc_security_group_ingress_rule" "allow_db_traffic" {
  ip_protocol                  = "tcp"
  security_group_id            = aws_security_group.db_sg.id
  from_port                    = 5432
  to_port                      = 5432
  referenced_security_group_id = aws_security_group.api_sg.id
}

resource "aws_eip" "eip_assoc" {
  instance = aws_instance.api_server.id
  depends_on = [aws_internet_gateway.igw]
}