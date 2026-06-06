variable "aws_region" {
  description = "AWS region to deploy resources in"
  type        = string
  default     = "ca-central-1"
}

variable "project_name" {
  description = "Project name used for tagging resources"
  type        = string
  default     = "remember-birthdays"
}

variable "db_password" {
  description = "RDS master password"
  type        = string
  sensitive   = true
}