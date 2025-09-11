
data "aws_s3_bucket" "bucket" {
  bucket = var.bucket_name
}
resource "aws_s3_bucket_policy" "assets_policy" {
  bucket = data.aws_s3_bucket.bucket.id
  policy = jsonencode(
    {
      Version = "2008-10-17"
      Statement = [
        {
          Sid    = "AllowUserPutGetObject"
          Effect = "Allow"
          Principal = {
            AWS = data.aws_iam_user.app_user.arn
          }
          Action = [
            "s3:PutObject",
            "s3:GetObject",
          ]
          Resource = "${data.aws_s3_bucket.bucket.arn}/*"
        },
        {
          Sid    = "AllowUserListObject"
          Effect = "Allow"
          Principal = {
            AWS = data.aws_iam_user.app_user.arn
          }
          Action = [
            "s3:ListBucket"
          ]
          Resource = data.aws_s3_bucket.bucket.arn
        }
      ],
  })
}
