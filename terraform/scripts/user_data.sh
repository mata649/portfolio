#!/bin/bash
sudo tee /usr/local/bin/fetch-ssm-parameters.sh > /dev/null <<'EOF'
#!/bin/bash
DB_USER=$(aws ssm get-parameter --name ${ssm_parameter_db_user} --with-decryption --query Parameter.Value --output text)
DB_PASSWORD=$(aws ssm get-parameter --name ${ssm_parameter_db_password} --with-decryption --query Parameter.Value --output text)
DB_HOST=$(aws ssm get-parameter --name ${ssm_parameter_db_host} --with-decryption --query Parameter.Value --output text)
DB_PORT=$(aws ssm get-parameter --name ${ssm_parameter_db_port} --with-decryption --query Parameter.Value --output text)
DB_NAME=$(aws ssm get-parameter --name ${ssm_parameter_db_name} --with-decryption --query Parameter.Value --output text)
JWT_SIGNING_KEY=$(aws ssm get-parameter --name ${ssm_parameter_jwt_signing_key} --with-decryption --query Parameter.Value --output text)
ADMIN_EMAIL=$(aws ssm get-parameter --name ${ssm_parameter_admin_email} --with-decryption --query Parameter.Value --output text)
ADMIN_PASSWORD=$(aws ssm get-parameter --name ${ssm_parameter_admin_password} --with-decryption --query Parameter.Value --output text)
WEB_PORT=$(aws ssm get-parameter --name ${ssm_parameter_web_port} --with-decryption --query Parameter.Value --output text)
echo "export DB_USER=$DB_USER" >> /etc/profile.d/ssm_parameters.sh
echo "export DB_PASSWORD=$DB_PASSWORD" >> /etc/profile.d/ssm_parameters.sh
echo "export DB_HOST=$DB_HOST" >> /etc/profile.d/ssm_parameters.sh
echo "export DB_PORT=$DB_PORT" >> /etc/profile.d/ssm_parameters.sh
echo "export DB_NAME=$DB_NAME" >> /etc/profile.d/ssm_parameters.sh
echo "export JWT_SIGNING_KEY=$JWT_SIGNING_KEY" >> /etc/profile.d/ssm_parameters.sh
echo "export ADMIN_EMAIL=$ADMIN_EMAIL" >> /etc/profile.d/ssm_parameters.sh
echo "export ADMIN_PASSWORD=$ADMIN_PASSWORD" >> /etc/profile.d/ssm_parameters.sh
echo "export WEB_PORT=$WEB_PORT" >> /etc/profile.d/ssm_parameters.sh

EOF
sudo chmod +x /usr/local/bin/fetch-ssm-parameters.sh

sudo tee /etc/systemd/system/fetch-ssm-parameters.service > /dev/null <<'EOF'
[Unit]
Description=Fetch Parameters from SSM at boot
After=network.target

[Service]
Type=oneshot
ExecStart=/usr/local/bin/fetch-ssm-parameters.sh
RemainAfterExit=true

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable fetch-ssm-parameters.service
sudo systemctl start fetch-ssm-parameters.service

sudo tee /etc/systemd/system/portfolio_api.service > /dev/null <<'EOF'
[Unit]
Description=Portfolio API
After=network.target fetch-ssm-parameters.service

[Service]
User=ec2-user
WorkingDirectory=/home/ec2-user
ExecStart=/home/ec2-user/portfolio_api
Restart=always
AmbientCapabilities=CAP_NET_BIND_SERVICE
EnvironmentFile=/etc/profile.d/ssm_parameters.sh

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable --now portfolio_api
