FROM elixir:1.17-alpine as builder
RUN apk add --update alpine-sdk
ENV MIX_ENV=prod
COPY lib ./lib
COPY mix.exs .
COPY mix.lock .
RUN mix local.rebar --force \
    && mix local.hex --force \
    && mix deps.get \
    && mix release

# ---- Application Stage ----
FROM alpine:3.9.6
RUN apk add --no-cache --update bash openssl
WORKDIR /app
COPY --from=builder _build/prod/rel/portfolio/ .
CMD ["/app/bin/portfolio", "start"]