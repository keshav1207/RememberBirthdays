resource "aws_db_subnet_group" "main" {
  name        = "default-vpc-04d9056986ead42f5"
  description = "Created from the RDS Management Console"
  subnet_ids  = var.subnet_ids

  tags = {
    Name    = "${var.project_name}-db-subnet-group"
    Project = var.project_name
  }
}

resource "aws_db_instance" "main" {
  identifier              = "remember-birthdays-db"
  engine                  = "postgres"
  engine_version          = "18.3"
  instance_class          = "db.t4g.micro"
  allocated_storage       = 20
  max_allocated_storage   = 1000
  storage_type            = "gp2"
  storage_encrypted       = true
  db_name                 = "rememberbirthdays"
  username                = "postgres"
  password                = var.db_password
  publicly_accessible     = false
  multi_az                = false
  backup_retention_period = 1
  skip_final_snapshot     = true
  deletion_protection     = false
  auto_minor_version_upgrade = true
  copy_tags_to_snapshot   = true

  db_subnet_group_name   = aws_db_subnet_group.main.name
  vpc_security_group_ids = [var.security_group_id]

  lifecycle {
    ignore_changes = [password]
  }

  tags = {
    Name    = "${var.project_name}-db"
    Project = var.project_name
  }
}