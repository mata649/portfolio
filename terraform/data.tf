data "aws_ssm_parameter" "portfolio_db_username" {
  name = var.ssm_parameter_db_username
}
data "aws_ssm_parameter" "portfolio_db_password" {
  name            = var.ssm_parameter_db_password
  with_decryption = true
}

data "aws_ssm_parameter" "portfolio_jwt_signing_key" {
  name            = var.ssm_parameter_jwt_signing_key
  with_decryption = true
}
data "aws_ssm_parameter" "portfolio_admin_email" {
  name = var.ssm_parameter_admin_email
}
data "aws_ssm_parameter" "portfolio_admin_password" {
  name            = var.ssm_parameter_admin_password
  with_decryption = true
}
data "aws_ssm_parameter" "portfolio_web_port" {
  name            = var.ssm_parameter_web_port
}
data "aws_key_pair" "portfolio_key" {
  key_name = "portfolio-access-key"
}
