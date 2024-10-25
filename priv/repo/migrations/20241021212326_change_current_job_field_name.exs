defmodule Portfolio.Repo.Migrations.ChangeCurrentJobFieldName do
  use Ecto.Migration

  def change do
    rename table(:experiences), :currentJob, to: :current_job
  end
end
