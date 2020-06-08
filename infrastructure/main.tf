provider "azurerm" {
  version = "=1.44.0"
}

locals {
  app_full_name = "${var.product}-${var.component}"

  aseName = "core-compute-${var.env}"
  local_env = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env}"
  local_ase = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "core-compute-aat" : "core-compute-saat" : local.aseName}"
  env_ase_url = "${local.local_env}.service.${local.local_ase}.internal"

  // Vault name
  previewVaultName = "${var.raw_product}-aat"
  nonPreviewVaultName = "${var.raw_product}-${var.env}"
  vaultName = "${(var.env == "preview" || var.env == "spreview") ? local.previewVaultName : local.nonPreviewVaultName}"

  // Shared Resource Group
  previewResourceGroup = "${var.raw_product}-shared-infrastructure-aat"
  nonPreviewResourceGroup = "${var.raw_product}-shared-infrastructure-${var.env}"
  sharedResourceGroup = "${(var.env == "preview" || var.env == "spreview") ? local.previewResourceGroup : local.nonPreviewResourceGroup}"

  sharedAppServicePlan = "${var.raw_product}-${var.env}"
  sharedASPResourceGroup = "${var.raw_product}-shared-${var.env}"

  // S2S
  s2s_url = "http://rpe-service-auth-provider-${local.env_ase_url}"
  idam_url = "https://idam-api.${local.local_env}.platform.hmcts.net"

  definition_store_host = "http://ccd-definition-store-api-${local.env_ase_url}"
  }

data "azurerm_key_vault" "am_key_vault" {
  name = local.vaultName
  resource_group_name = local.sharedResourceGroup
}

data "azurerm_key_vault" "s2s_vault" {
  name = "s2s-${local.local_env}"
  resource_group_name = "rpe-service-auth-provider-${local.local_env}"
}

data "azurerm_key_vault_secret" "s2s_secret" {
  name = "microservicekey-am-judicial-booking-service"
  key_vault_id = data.azurerm_key_vault.s2s_vault.id
}

resource "azurerm_key_vault_secret" "am_judicial_booking_service_s2s_secret" {
  name = "am-judicial-booking-service-s2s-secret"
  value = data.azurerm_key_vault_secret.s2s_secret.value
  key_vault_id = data.azurerm_key_vault.am_key_vault.id
}


module "judicial-booking-db" {
  source = "git@github.com:hmcts/cnp-module-postgres?ref=master"
  product = "${local.app_full_name}-postgres-db"
  location = "${var.location}"
  env = "${var.env}"
  subscription = "${var.subscription}"
  postgresql_user = "${var.postgresql_user}"
  database_name = "${var.database_name}"
  sku_name = "${var.database_sku_name}"
  sku_tier = "GeneralPurpose"
  sku_capacity = "${var.database_sku_capacity}"
  storage_mb = "${var.database_storage_mb}"
  common_tags  = "${var.common_tags}"
}

////////////////////////////////
// Populate Vault with DB info
////////////////////////////////

resource "azurerm_key_vault_secret" "POSTGRES-USER" {
  name = "${local.app_full_name}-POSTGRES-USER"
  value = "${module.judicial-booking-db.user_name}"
  key_vault_id = "${data.azurerm_key_vault.am_key_vault.id}"
}

resource "azurerm_key_vault_secret" "POSTGRES-PASS" {
  name = "${local.app_full_name}-POSTGRES-PASS"
  value = "${module.judicial-booking-db.postgresql_password}"
  key_vault_id = "${data.azurerm_key_vault.am_key_vault.id}"
}

resource "azurerm_key_vault_secret" "POSTGRES_HOST" {
  name = "${local.app_full_name}-POSTGRES-HOST"
  value = "${module.judicial-booking-db.host_name}"
  key_vault_id = "${data.azurerm_key_vault.am_key_vault.id}"
}

resource "azurerm_key_vault_secret" "POSTGRES_PORT" {
  name = "${local.app_full_name}-POSTGRES-PORT"
  value = "${module.judicial-booking-db.postgresql_listen_port}"
  key_vault_id = "${data.azurerm_key_vault.am_key_vault.id}"
}

resource "azurerm_key_vault_secret" "POSTGRES_DATABASE" {
  name = "${local.app_full_name}-POSTGRES-DATABASE"
  value = "${module.judicial-booking-db.postgresql_database}"
  key_vault_id = "${data.azurerm_key_vault.am_key_vault.id}"
}
