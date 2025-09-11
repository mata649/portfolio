resource "aws_cloudfront_distribution" "distribution" {
  enabled = true
  origin {
    domain_name = aws_instance.server.public_dns
    origin_id   = aws_instance.server.id
    custom_origin_config {
      http_port              = 80
      https_port             = 443
      origin_protocol_policy = "match-viewer"
      origin_ssl_protocols   = ["TLSv1.2"]
    }
  }
  origin {
    origin_id                = data.aws_s3_bucket.bucket.id
    domain_name              = data.aws_s3_bucket.bucket.bucket_regional_domain_name
    origin_access_control_id = aws_cloudfront_origin_access_control.web.id
  }
  default_cache_behavior {
    target_origin_id       = aws_instance.server.id
    viewer_protocol_policy = "allow-all"
    allowed_methods        = ["HEAD", "DELETE", "POST", "GET", "OPTIONS", "PUT", "PATCH"]
    cached_methods         = ["GET", "HEAD"]

    compress = true
    forwarded_values {
      query_string = true
      cookies {
        forward = "all"
      }

    }
    min_ttl     = 0
    default_ttl = 3600
    max_ttl     = 86400

  }
  ordered_cache_behavior {
    path_pattern           = "/static/*"
    target_origin_id       = data.aws_s3_bucket.bucket.id
    viewer_protocol_policy = "redirect-to-https"

    allowed_methods = ["GET", "HEAD"]
    cached_methods  = ["GET", "HEAD"]
    cache_policy_id = data.aws_cloudfront_cache_policy.caching_optimized_policy.id
  }
  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }
  viewer_certificate {
    cloudfront_default_certificate = true
  }
}

data "aws_cloudfront_cache_policy" "caching_optimized_policy" {
  name = "Managed-CachingOptimized"
}

resource "aws_cloudfront_origin_access_control" "web" {
  name                              = "${local.prefix}OAC"
  origin_access_control_origin_type = "s3"
  signing_behavior                  = "always"
  signing_protocol                  = "sigv4"
}
