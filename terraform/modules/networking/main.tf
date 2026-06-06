resource "aws_vpc" "main" {
  cidr_block           = "172.31.0.0/16"
  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = {
    Name    = "${var.project_name}-vpc"
    Project = var.project_name
  }
}

resource "aws_subnet" "public_a" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "172.31.16.0/20"
  availability_zone       = "ca-central-1a"
  map_public_ip_on_launch = true

  tags = {
    Name    = "${var.project_name}-subnet-public-a"
    Project = var.project_name
  }
}

resource "aws_subnet" "public_b" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "172.31.0.0/20"
  availability_zone       = "ca-central-1b"
  map_public_ip_on_launch = true

  tags = {
    Name    = "${var.project_name}-subnet-public-b"
    Project = var.project_name
  }
}

resource "aws_subnet" "public_d" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "172.31.32.0/20"
  availability_zone       = "ca-central-1d"
  map_public_ip_on_launch = true

  tags = {
    Name    = "${var.project_name}-subnet-public-d"
    Project = var.project_name
  }
}

resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name    = "${var.project_name}-igw"
    Project = var.project_name
  }
}

resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main.id
  }

  tags = {
    Name    = "${var.project_name}-rt-public"
    Project = var.project_name
  }
}