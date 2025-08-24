region         = "us-east-1"
project_name   = "Portfolio"
vpc_cidr_block = "10.32.0.0/16"

web_site_bucket_name = "mata649.com"

public_subnets = {
  "subnet-1" = {
    cidr = "10.32.0.0/20",
    az   = "us-east-1a"
  },
  "subnet-2" = {
    cidr = "10.32.16.0/20",
    az   = "us-east-1b"
  }
}

private_subnets = {
  "subnet-1" = {
    cidr = "10.32.32.0/20",
    az   = "us-east-1a"
  },
  "subnet-2" = {
    cidr = "10.32.48.0/20",
    az   = "us-east-1b"
  }
}

api_instance_specs = {
  ami           = "ami-0de716d6197524dd9"
  instance_type = "t3.micro"
  volume_size   = 8
  volume_type   = "gp3"
  volume_iops   = 3000
}

rds_db_specs = {
  identifier        = "portfolio-db"
  db_name           = "portfolio"
  engine            = "postgres"
  engine_version    = "17.6"
  instance_class    = "db.t4g.micro"
  allocated_storage = 20

}

ssm_parameter_db_username     = "/portfolio/db/user"
ssm_parameter_db_password     = "/portfolio/db/password"
ssm_parameter_db_host         = "/portfolio/db/host"
ssm_parameter_db_port         = "/portfolio/db/port"
ssm_parameter_db_name         = "/portfolio/db/name"
ssm_parameter_jwt_signing_key = "/portfolio/jwt/signing_key"
ssm_parameter_admin_email     = "/portfolio/admin/email"
ssm_parameter_admin_password  = "/portfolio/admin/password"
ssm_parameter_web_port        = "/portfolio/web/port"
