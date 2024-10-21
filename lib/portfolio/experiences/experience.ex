defmodule Portfolio.Experiences.Experience do
  use Ecto.Schema
  import Ecto.Changeset

  schema "experiences" do
    field :position, :string
    field :description, :string
    field :location, :string
    field :to, :date
    field :from, :date
    field :company, :string
    field :currentJob, :boolean, default: false

    timestamps(type: :utc_datetime)
  end

  @doc false
  def changeset(experience, attrs) do
    experience
    |> cast(attrs, [:company, :position, :description, :from, :to, :currentJob, :location])
    |> validate_required([:company, :position, :description, :from, :to, :currentJob, :location])
  end
end
