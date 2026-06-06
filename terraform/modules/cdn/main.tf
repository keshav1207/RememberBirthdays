resource "aws_cloudfront_distribution" "frontend" {
  enabled             = true
  is_ipv6_enabled     = true
  http_version        = "http2"
  price_class         = "PriceClass_All"

  origin {
    domain_name              = var.s3_bucket_domain
    origin_id                = "rememberbithdays-frontend.s3.ca-central-1.amazonaws.com-mpoa0gl9jfk"
    origin_access_control_id = "E1CFQLT7RWL6HU"


  }

  default_cache_behavior {
    target_origin_id       = "rememberbithdays-frontend.s3.ca-central-1.amazonaws.com-mpoa0gl9jfk"
    viewer_protocol_policy = "redirect-to-https"
    allowed_methods        = ["GET", "HEAD"]
    cached_methods         = ["GET", "HEAD"]
    compress               = true
    cache_policy_id        = "658327ea-f89d-4fab-a63d-7e88639e58f6"
  }

  custom_error_response {
    error_code            = 403
    response_page_path    = "/index.html"
    response_code         = "200"
    error_caching_min_ttl = 10
  }

  custom_error_response {
    error_code            = 404
    response_page_path    = "/index.html"
    response_code         = "200"
    error_caching_min_ttl = 10
  }

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  viewer_certificate {
    cloudfront_default_certificate = true
  }

  tags = {
    Name    = "${var.project_name}-frontend-cdn"
    Project = var.project_name
  }
}

resource "aws_cloudfront_distribution" "auth" {
  enabled             = true
  is_ipv6_enabled     = true
  http_version        = "http2"
  price_class         = "PriceClass_All"

  origin {
    domain_name = var.ec2_domain
    origin_id   = "ec2-3-99-225-134.ca-central-1.compute.amazonaws.com-mpptr7hpgvd"

    custom_origin_config {
      http_port              = 80
      https_port             = 443
      origin_protocol_policy = "http-only"
      origin_ssl_protocols   = ["SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"]
    }
  }

  default_cache_behavior {
    target_origin_id         = "ec2-3-99-225-134.ca-central-1.compute.amazonaws.com-mpptr7hpgvd"
    viewer_protocol_policy   = "redirect-to-https"
    allowed_methods          = ["DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"]
    cached_methods           = ["GET", "HEAD"]
    compress                 = true
    cache_policy_id          = "4135ea2d-6df8-44a3-9df3-4b5a84be39ad"
    origin_request_policy_id = "216adef6-5c7f-47e4-b989-5492eafa07d3"
  }

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  viewer_certificate {
    cloudfront_default_certificate = true
  }

  tags = {
    Name    = "${var.project_name}-auth-cdn"
    Project = var.project_name
  }
}

resource "aws_cloudfront_distribution" "api" {
  enabled             = true
  is_ipv6_enabled     = true
  http_version        = "http2"
  price_class         = "PriceClass_All"

  origin {
    domain_name = var.ec2_domain
    origin_id   = "ec2-3-99-225-134.ca-central-1.compute.amazonaws.com-mppv5ycbobt"

    custom_origin_config {
      http_port              = 8080
      https_port             = 443
      origin_protocol_policy = "http-only"
      origin_ssl_protocols   = ["SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"]
    }
  }

  default_cache_behavior {
    target_origin_id         = "ec2-3-99-225-134.ca-central-1.compute.amazonaws.com-mppv5ycbobt"
    viewer_protocol_policy   = "redirect-to-https"
    allowed_methods          = ["DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"]
    cached_methods           = ["GET", "HEAD"]
    compress                 = true
    cache_policy_id          = "4135ea2d-6df8-44a3-9df3-4b5a84be39ad"
    origin_request_policy_id = "216adef6-5c7f-47e4-b989-5492eafa07d3"
  }

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  viewer_certificate {
    cloudfront_default_certificate = true
  }

  tags = {
    Name    = "${var.project_name}-api-cdn"
    Project = var.project_name
  }
}