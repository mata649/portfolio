#!/bin/bash
set -e

sudo tee /usr/local/bin/fetch-ssm-parameters.sh > /dev/null <<'EOF'
#!/bin/bash
# Fetch parameters from AWS SSM and write to systemd-compatible env file
DB_USER=$(aws ssm get-parameter --name "${ssm_parameter_db_user}" --with-decryption --query Parameter.Value --output text)
DB_PASSWORD=$(aws ssm get-parameter --name "${ssm_parameter_db_password}" --with-decryption --query Parameter.Value --output text)
DB_HOST=$(aws ssm get-parameter --name "${ssm_parameter_db_host}" --with-decryption --query Parameter.Value --output text)
DB_PORT=$(aws ssm get-parameter --name "${ssm_parameter_db_port}" --with-decryption --query Parameter.Value --output text)
DB_NAME=$(aws ssm get-parameter --name "${ssm_parameter_db_name}" --with-decryption --query Parameter.Value --output text)
JWT_SIGNING_KEY=$(aws ssm get-parameter --name "${ssm_parameter_jwt_signing_key}" --with-decryption --query Parameter.Value --output text)
ADMIN_EMAIL=$(aws ssm get-parameter --name "${ssm_parameter_admin_email}" --with-decryption --query Parameter.Value --output text)
ADMIN_PASSWORD=$(aws ssm get-parameter --name "${ssm_parameter_admin_password}" --with-decryption --query Parameter.Value --output text)
WEB_PORT=$(aws ssm get-parameter --name "${ssm_parameter_web_port}" --with-decryption --query Parameter.Value --output text)

cat <<EOT > /etc/profile.d/ssm_parameters.sh
DB_USER=$DB_USER
DB_PASSWORD=$DB_PASSWORD
DB_HOST=$DB_HOST
DB_PORT=$DB_PORT
DB_NAME=$DB_NAME
JWT_SIGNING_KEY=$JWT_SIGNING_KEY
ADMIN_EMAIL=$ADMIN_EMAIL
ADMIN_PASSWORD=$ADMIN_PASSWORD
WEB_PORT=$WEB_PORT
EOT
EOF

sudo chmod +x /usr/local/bin/fetch-ssm-parameters.sh

sudo tee /etc/systemd/system/fetch-ssm-parameters.service > /dev/null <<'EOF'
[Unit]
Description=Fetch SSM Parameters
After=network.target

[Service]
Type=oneshot
ExecStart=/usr/local/bin/fetch-ssm-parameters.sh
RemainAfterExit=true

[Install]
WantedBy=multi-user.target
EOF

sudo tee /etc/systemd/system/portfolio_api.service > /dev/null <<'EOF'
[Unit]
Description=Portfolio API Service
After=network.target fetch-ssm-parameters.service
Requires=fetch-ssm-parameters.service

[Service]
User=ec2-user
WorkingDirectory=/home/ec2-user
EnvironmentFile=/etc/profile.d/ssm_parameters.sh
ExecStart=/home/ec2-user/portfolio_api
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable fetch-ssm-parameters
sudo systemctl enable portfolio_api

sudo systemctl start fetch-ssm-parameters
sudo systemctl start portfolio_api
