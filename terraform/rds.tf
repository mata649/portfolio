resource "aws_db_instance" "db" {
  identifier           = var.rds_db_specs.identifier
  db_name              = var.rds_db_specs.db_name
  engine               = var.rds_db_specs.engine
  engine_version       = var.rds_db_specs.engine_version
  instance_class       = var.rds_db_specs.instance_class
  allocated_storage    = var.rds_db_specs.allocated_storage
  password             = data.aws_ssm_parameter.portfolio_db_password.value
  username             = data.aws_ssm_parameter.portfolio_db_username.value
  publicly_accessible  = false
  multi_az             = false
  skip_final_snapshot = true
  vpc_security_group_ids = [aws_security_group.db_sg.id]
  db_subnet_group_name = aws_db_subnet_group.db_subnet_group.name
  tags = {
    Name : "${local.prefix} DB"
  }
}

resource "aws_db_subnet_group" "db_subnet_group" {
  name = "${lower(local.prefix)}-db-subnet-group"
  subnet_ids = [for subnet in aws_subnet.private_subnets : subnet.id]
  tags = {
    Name : "${local.prefix} DB Subnet group"
  }
}

resource "aws_ssm_parameter" "portfolio_db_host" {
  name  = var.ssm_parameter_db_host
  type  = "String"
  value = aws_db_instance.db.endpoint
}
resource "aws_ssm_parameter" "portfolio_db_port" {
  name = var.ssm_parameter_db_port
  type = "String"
  value = aws_db_instance.db.port
}
resource "aws_ssm_parameter" "portfolio_db_name" {
  name = var.ssm_parameter_db_name
  type = "String"
  value = aws_db_instance.db.db_name
}