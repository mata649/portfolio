resource "aws_cloudfront_distribution" "portfolio_distribution" {
  enabled = true
  origin {
    domain_name = aws_instance.api_server.public_dns
    origin_id   = aws_instance.api_server.id
    custom_origin_config {
      http_port              = 80
      https_port             = 443
      origin_protocol_policy = "match-viewer"
      origin_ssl_protocols = ["TLSv1.2"]
    }
  }
  default_root_object = "index.html"
  origin {
    origin_id                = aws_s3_bucket.web.id
    domain_name              = aws_s3_bucket.web.bucket_regional_domain_name
    origin_access_control_id = aws_cloudfront_origin_access_control.web.id
  }

  default_cache_behavior {
    target_origin_id       = aws_s3_bucket.web.id
    viewer_protocol_policy = "allow-all"
    allowed_methods = ["GET", "HEAD", "OPTIONS"]
    cached_methods = ["GET", "HEAD"]
    compress               = true
    cache_policy_id        = data.aws_cloudfront_cache_policy.caching_optimized_policy.id

  }

  ordered_cache_behavior {
    path_pattern           = "/api/*"
    target_origin_id       = aws_instance.api_server.id
    viewer_protocol_policy = "allow-all"
    allowed_methods = ["HEAD", "DELETE", "POST", "GET", "OPTIONS", "PUT", "PATCH"]
    cached_methods = ["GET", "HEAD"]

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