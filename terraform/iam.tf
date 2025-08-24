resource "aws_iam_policy" "db_connect_policy" {
  name        = "allow-db-connection"
  description = "Provides the permissions to connect to the DB"
  policy = jsonencode(
    {
      "Version" = "2012-10-17"
      "Statement" = [
        {
          "Effect" = "Allow"
          "Action" = [
            "rds-db:connect"
          ]
          "Resource" = [
            "${aws_db_instance.db.arn}/${aws_db_instance.db.db_name}"
          ]
        }
      ]
    }
  )
}

resource "aws_iam_policy" "get_ssm_parameters_policy" {
  name        = "allow-get-ssm-parameters"
  description = "Provides the permissions to get the db ssm parameters"
  policy = jsonencode(
    {
      "Version" = "2012-10-17"
      "Statement" = [
        {
          "Effect" = "Allow"
          "Action" = [
            "ssm:GetParametersByPath",
            "ssm:GetParameter"
          ],
          "Resource" = [
            data.aws_ssm_parameter.portfolio_db_username.arn,
            data.aws_ssm_parameter.portfolio_db_password.arn,
            data.aws_ssm_parameter.portfolio_jwt_signing_key.arn,
            data.aws_ssm_parameter.portfolio_admin_email.arn,
            data.aws_ssm_parameter.portfolio_admin_password.arn,
            data.aws_ssm_parameter.portfolio_web_port.arn,
            aws_ssm_parameter.portfolio_db_host.arn,
            aws_ssm_parameter.portfolio_db_port.arn,
            aws_ssm_parameter.portfolio_db_name.arn,
          ]
        }
      ]
    }
  )
}

resource "aws_iam_policy_attachment" "attach_db_connect" {
  name       = "attach_db_policy_to_api_role"
  roles = [aws_iam_role.api_role.name]
  policy_arn = aws_iam_policy.db_connect_policy.arn
}

resource "aws_iam_policy_attachment" "attach_get_ssm_parameters" {
  name       = "attach_ssm_parameter_policy_to_api_role"
  roles = [aws_iam_role.api_role.name]
  policy_arn = aws_iam_policy.get_ssm_parameters_policy.arn
}

resource "aws_iam_role" "api_role" {
  name = "ec2-api-role"
  assume_role_policy = jsonencode({
    "Version" = "2012-10-17",
    "Statement" = [
      {
        "Effect" = "Allow",
        "Action" = [
          "sts:AssumeRole"
        ],
        "Principal" = {
          "Service" = [
            "ec2.amazonaws.com"
          ]
        }
      }
    ]
  })
}
resource "aws_iam_instance_profile" "api_instance_profile" {
  name = "ec2-api-instance-profile"
  role = aws_iam_role.api_role.name
}

data "aws_iam_user" "admin_user" {
  user_name = "adminuser"
}