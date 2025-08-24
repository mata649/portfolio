resource "aws_s3_bucket" "web" {
  bucket        = var.web_site_bucket_name
  force_destroy = true
}

resource "aws_s3_bucket_public_access_block" "web_public_access" {
  bucket                  = aws_s3_bucket.web.id
  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
}
resource "aws_s3_bucket_policy" "web_policy" {
  bucket = aws_s3_bucket.web.id
  policy = jsonencode({
    Version = "2008-10-17"
    Statement = [
      {
        Sid    = "AllowCloudFrontServicePrincipal"
        Effect = "Allow"
        Principal = {
          Service = "cloudfront.amazonaws.com"
        }
        Action   = "s3:GetObject"
        Resource = "${aws_s3_bucket.web.arn}/*"
        Condition = {
          StringEquals = {
            "AWS:SourceArn" = aws_cloudfront_distribution.portfolio_distribution.arn
          }
        }
      },
      {
        Sid    = "AllowAdminUserPush"
        Effect = "Allow"
        Principal = {
          AWS = data.aws_iam_user.admin_user.arn
        }
        Action   = "s3:PutObject"
        Resource = "${aws_s3_bucket.web.arn}/*"
      }
    ]
  })
}

/*resource "aws_s3_bucket_website_configuration" "web_config" {
  bucket = aws_s3_bucket.web.id
  index_document {
    suffix = "index.html"
  }
  error_document {
    key = "error.html"
  }

}*/