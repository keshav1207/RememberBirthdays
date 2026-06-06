terraform {
  backend "s3" {
    bucket = "remember-birthdays-terraform-state"
    key    = "terraform.tfstate"
    region = "ca-central-1"
  }
}