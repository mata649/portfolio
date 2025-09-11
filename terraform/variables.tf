variable "project_name" {
  default     = "Portfolio"
  description = "The name of the project"
  type        = string
}
variable "bucket_name" {
  description = "The bucket name"
  type        = string
}

variable "app_user" {
  description = "The iam user name used by the app"
  type        = string
}
variable "region" {
  default = "us-east-1"
  type    = string
}
variable "domain" {
  description = "The portfolio domain"
  type        = string
}
variable "vpc_cidr" {
  description = "The CIDR(network block) of the VPC"
  type        = string
}

variable "tags" {
  description = "The tags used on the resources(env, project, etc)"

}
variable "public_subnets" {
  description = "The configuration for the public subnets"
  type = map(object({
    name = string
    cidr = string
    az   = string
  }))
}

variable "ec2_instance_specs" {
  description = "The EC2 instance specs"
  type = object({
    ami           = string
    instance_type = string
    volume_size   = number
    volume_type   = string
    volume_iops   = number
  })
}
