
resource "aws_instance" "server" {
  ami                         = var.ec2_instance_specs.ami
  instance_type               = var.ec2_instance_specs.instance_type
  associate_public_ip_address = true
  subnet_id                   = aws_subnet.public_subnets["subnet-1"].id
  enable_primary_ipv6         = false
  vpc_security_group_ids      = [aws_security_group.server_sg.id]
  user_data = file("./scripts/user_data.sh")
  root_block_device {
    volume_size           = var.ec2_instance_specs.volume_size
    volume_type           = var.ec2_instance_specs.volume_type
    iops                  = var.ec2_instance_specs.volume_iops
    delete_on_termination = true
    tags = {
      Name : "${local.prefix} Server Root EBS"
    }
  }
  tags = {
    Name : "${local.prefix} Server"
  }
}
