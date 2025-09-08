locals {
    prefix = "${var.project_name} ${var.tags["Enviroment"]}"
    common_tags = var.tags
}
