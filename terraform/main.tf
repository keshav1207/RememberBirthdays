module "networking" {
  source       = "./modules/networking"
  project_name = var.project_name
}

module "security" {
  source       = "./modules/security"
  project_name = var.project_name
  vpc_id       = module.networking.vpc_id
}

module "compute" {
  source            = "./modules/compute"
  project_name      = var.project_name
  subnet_id         = module.networking.subnet_public_b_id
  security_group_id = module.security.ec2_sg_id
}

module "database" {
  source            = "./modules/database"
  project_name      = var.project_name
  security_group_id = module.security.rds_sg_id
  db_password       = var.db_password
  subnet_ids        = [
    module.networking.subnet_public_a_id,
    module.networking.subnet_public_b_id,
    module.networking.subnet_public_d_id,
  ]
}

module "storage" {
  source       = "./modules/storage"
  project_name = var.project_name
}

module "cdn" {
  source           = "./modules/cdn"
  project_name     = var.project_name
  s3_bucket_domain = "rememberbithdays-frontend.s3.ca-central-1.amazonaws.com"
  ec2_domain       =  "ec2-35-182-20-4.ca-central-1.compute.amazonaws.com"
}