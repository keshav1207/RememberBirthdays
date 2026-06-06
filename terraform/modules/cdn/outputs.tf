output "frontend_domain" {
  value = aws_cloudfront_distribution.frontend.domain_name
}

output "api_domain" {
  value = aws_cloudfront_distribution.api.domain_name
}

output "auth_domain" {
  value = aws_cloudfront_distribution.auth.domain_name
}