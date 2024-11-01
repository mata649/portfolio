# Script for populating the database. You can run it as:
#
#     mix run priv/repo/seeds.exs
#
# Inside the script, you can read and write to any of your
# repositories directly:
#
#     Portfolio.Repo.insert!(%Portfolio.SomeSchema{})
#
# We recommend using the bang functions (`insert!`, `update!`
# and so on) as they will fail if something goes wrong.
alias Portfolio.Accounts
alias Portfolio.Repo
alias Portfolio.Accounts.User

if !Accounts.get_user_by_email(System.get_env("ADMIN_EMAIL")) do
  Repo.insert!(
  User.registration_changeset(%User{}, %{
    email: System.get_env("ADMIN_EMAIL"),
    password: System.get_env("ADMIN_PASSWORD")
  })
)
end
