terraform {
  required_version = ">= 1.0.0"

  backend "s3" {
    region  = "us-east-1"
    bucket  = "us-east-1-prod-terraform-state-fynemxezqlgs"
    key     = "terraform.tfstate"
    profile = ""
    encrypt = "true"

    dynamodb_table = "us-east-1-prod-terraform-state-fynemxezqlgs-lock"
  }
}
