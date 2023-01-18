export AZ_RESOURCE_GROUP=xiada-rg
export AZ_DATABASE_SERVER_NAME=xiada-psql
export AZ_DATABASE_NAME=demo
export AZ_LOCATION=westus
export AZ_LOCAL_IP_ADDRESS=$(curl -4 ifconfig.co)
export CURRENT_USERNAME=$(az ad signed-in-user show --query userPrincipalName -o tsv)
export CURRENT_USER_OBJECTID=$(az ad signed-in-user show --query id -o tsv)