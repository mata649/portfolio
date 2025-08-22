variable "region" {
  type = string
}
variable "project_name" {
  description = "The name of the project"
  type        = string
}

variable "vpc_cidr_block" {
  description = "The CIDR Block for the VPC"
  type        = string
}

variable "public_subnets" {
  description = "A list of objects containing the public subnets information"
  type = map(
    object({
      cidr = string
      az   = string
    })
  )
}

variable "private_subnets" {
  description = "A list of objects containing the private subnets information"
  type = map(
    object({
      cidr = string
      az   = string
    })
  )
}

variable "api_instance_specs" {
  description = "The API EC2 specs"
  type = object({
    ami           = string
    instance_type = string
    volume_size   = number
    volume_type   = string
    volume_iops   = number
  })
}

variable "rds_db_specs" {
  description = "The RDS DB specs"
  type = object({
    identifier        = string
    db_name           = string
    engine            = string
    engine_version    = string
    instance_class    = string
    allocated_storage = number
  })
}

variable "ssm_parameter_db_username" {
  type = string
}
variable "ssm_parameter_db_password" {
  type = string
}
variable "ssm_parameter_db_host" {
  type = string
}
variable "ssm_parameter_db_port" {
  type = string
}
variable "ssm_parameter_db_name" {
  type = string
}
variable "ssm_parameter_jwt_signing_key" {
  type = string
}
variable "ssm_parameter_admin_email" {
  type = string
}
variable "ssm_parameter_admin_password" {
  type = string
}
variable "ssm_parameter_web_port" {
  type = string
}
