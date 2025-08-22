resource "aws_instance" "api_server" {
  ami                         = var.api_instance_specs.ami
  instance_type               = var.api_instance_specs.instance_type
  associate_public_ip_address = true
  subnet_id                   = aws_subnet.public_subnets["subnet-1"].id
  vpc_security_group_ids = [aws_security_group.api_sg.id]
  iam_instance_profile        = aws_iam_instance_profile.api_instance_profile.name
  key_name                    = data.aws_key_pair.portfolio_key.key_name
  user_data = templatefile("./scripts/user_data.sh", {
    ssm_parameter_db_user         = var.ssm_parameter_db_username
    ssm_parameter_db_password     = var.ssm_parameter_db_password
    ssm_parameter_db_host         = var.ssm_parameter_db_host
    ssm_parameter_db_port         = var.ssm_parameter_db_port
    ssm_parameter_db_name         = var.ssm_parameter_db_name
    ssm_parameter_jwt_signing_key = var.ssm_parameter_jwt_signing_key
    ssm_parameter_admin_email     = var.ssm_parameter_admin_email
    ssm_parameter_admin_password  = var.ssm_parameter_admin_password
    ssm_parameter_web_port        = var.ssm_parameter_web_port
  })
  root_block_device {
    volume_size           = var.api_instance_specs.volume_size
    volume_type           = var.api_instance_specs.volume_type
    iops                  = var.api_instance_specs.volume_iops
    delete_on_termination = true
    tags = {
      Name : "${local.prefix} API Root EBS"
    }
  }
  tags = {
    Name : "${local.prefix} API"
  }
}
