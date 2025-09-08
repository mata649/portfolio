
data "aws_route53_zone" "hosted_zone" {
  name         = var.domain
  private_zone = false
}

resource "aws_route53_record" "alias" {
  zone_id = data.aws_route53_zone.hosted_zone.id
  name    = "cdn.${var.domain}"
  type    = "A"

  alias {
    name                   = aws_cloudfront_distribution.distribution.domain_name
    zone_id                = aws_cloudfront_distribution.distribution.hosted_zone_id
    evaluate_target_health = false
  }
}
