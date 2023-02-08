variable "product" {
  type = string
}

variable "raw_product" {
  type    = string
  default = "am"
}

variable "component" {
  type = string
}

variable "location" {
  type    = string
  default = "UK South"
}

variable "env" {
  type = string
}

variable "subscription" {
  type = string
}

variable "common_tags" {
  type = map(string)
}

variable "team_name" {
  type    = string
  default = "AM"
}

////////////////////////////////
// Database
////////////////////////////////

variable "postgresql_user" {
  type    = string
  default = "am"
}

variable "database_name" {
  type    = string
  default = "judicial_booking"
}

variable "database_sku_name" {
  type    = string
  default = "GP_Gen5_2"
}

variable "database_sku_capacity" {
  type    = string
  default = "2"
}

variable "database_storage_mb" {
  type    = string
  default = "51200"
}
