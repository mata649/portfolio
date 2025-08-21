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

  default_cache_behavior {
    target_origin_id = aws_instance.api_server.id
    allowed_methods = ["GET", "HEAD", "OPTIONS"]
    cached_methods = ["GET", "HEAD"]
    compress         = true
    forwarded_values {
      query_string = false
      cookies {
        forward = "none"
      }

    }
    viewer_protocol_policy = "allow-all"
    min_ttl                = 0
    default_ttl            = 3600
    max_ttl                = 86400

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