# These is the default configuration. This configuration applies only
# for the staging environment.
region       = "us-east-1"
project_name = "Portfolio"
vpc_cidr     = "10.16.0.0/16"
domain       = "mata649.com"
app_user     = "portfolio_app"
tags = {
  "Enviroment" = "Prod"
  "Project"    = "Portfolio"
}
public_subnets = {
  "subnet-1" = {
    name = "subnet-1"
    cidr = "10.16.0.0/20",
    az   = "us-east-1a"
  },
  "subnet-2" = {
    name = "subnet-2"
    cidr = "10.16.16.0/20",
    az   = "us-east-1b"
  }
}

bucket_name = "mata649.com"

ec2_instance_specs = {
  ami           = "ami-04360baab2ce7cf8c"
  instance_type = "t3.micro"
  volume_size   = 8
  volume_type   = "gp3"
  volume_iops   = 3000
}
