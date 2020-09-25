variable "product" {
}

variable "raw_product" {
  default = "am"
}

variable "component" {
}

variable "location" {
  default = "UK South"
}

variable "env" {
}

variable "subscription" {
}

variable "ilbIp" {}

variable "common_tags" {
  type = map(string)
}

variable "appinsights_instrumentation_key" {
  default = ""
}

variable "root_logging_level" {
  default = "INFO"
}

variable "log_level_spring_web" {
  default = "INFO"
}

variable "team_name" {
  default = "AM"
}

variable "managed_identity_object_id" {
  default = ""
}

variable "enable_ase" {
  default = false
}


variable "deployment_namespace" {}

////////////////////////////////
// Database
////////////////////////////////

variable "postgresql_user" {
  default = "am"
}

variable "database_name" {
  default = "judicial_booking"
}

variable "data_store_max_pool_size" {
  default = "16"
}

variable "database_sku_name" {
  default = "GP_Gen5_2"
}

variable "database_storage_mb" {
  default = "51200"
}

