resource "random_string" "bucket_suffix" {
    length  = 12
    special = false
    numeric = false
}

module "terraform_state_backend" {
    source                             = "cloudposse/tfstate-backend/aws"
    version                            = "1.6.0"
    stage                              = "prod"
    name                               = "terraform"
    attributes = ["state-${random_string.bucket_suffix.result}"]
    environment                        = var.region
    terraform_backend_config_file_path = "."
    terraform_backend_config_file_name = "backend.tf"
    force_destroy                      = false
}
