defmodule Portfolio.Skills.Skill do
  use Ecto.Schema
  import Ecto.Changeset

  schema "skills" do
    field :name, :string
    field :color, :string

    timestamps(type: :utc_datetime)
  end

  @doc false
  def changeset(skill, attrs) do
    skill
    |> cast(attrs, [:name, :color])
    |> validate_required([:name, :color])
    |> validate_length(:name, max: 15)
    |> validate_length(:color, is: 7)
    |> validate_format(:color, ~r/^#(?:[0-9a-fA-F]{3}){1,2}$/)
  end
end
