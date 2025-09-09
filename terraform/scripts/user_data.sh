#!/bin/bash
set -e

sudo yum update -y
sudo yum install git -y
sudo amazon-linux-extras install docker -y || sudo yum install -y docker
sudo systemctl enable docker
sudo systemctl start docker
sudo usermod -aG docker ec2-user

DOCKER_CONFIG=${DOCKER_CONFIG:-$HOME/.docker}
mkdir -p $DOCKER_CONFIG/cli-plugins
curl -SL https://github.com/docker/compose/releases/download/v2.29.2/docker-compose-linux-x86_64 \
    -o $DOCKER_CONFIG/cli-plugins/docker-compose
chmod +x $DOCKER_CONFIG/cli-plugins/docker-compose

cd /home/ec2-user
git clone https://github.com/mata649/portfolio.git
cd portfolio

docker compose -f docker-compose.yml -f docker-compose.prod.yml up -d --build
