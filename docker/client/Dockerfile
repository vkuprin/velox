FROM node:16.13.0-alpine

WORKDIR /app

ENV PATH /app/node_modules/.bin:$PATH

COPY package.json ./
COPY yarn.lock ./
RUN yarn install --silent

COPY . ./

CMD ["yarn", "start"]