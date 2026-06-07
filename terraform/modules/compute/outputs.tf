output "instance_id" {
  value = aws_instance.app.id
}

output "public_ip" {
  value = aws_instance.app.public_ip
}

output "elastic_ip" {
  value = aws_eip.main.public_ip
}