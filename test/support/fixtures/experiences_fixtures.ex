defmodule Portfolio.ExperiencesFixtures do
  @moduledoc """
  This module defines test helpers for creating
  entities via the `Portfolio.Experiences` context.
  """

  @doc """
  Generate a experience.
  """
  def experience_fixture(attrs \\ %{}) do
    {:ok, experience} =
      attrs
      |> Enum.into(%{
        company: "some company",
        currentJob: true,
        description: "some description",
        from: ~D[2024-10-20],
        location: "some location",
        position: "some position",
        to: ~D[2024-10-20]
      })
      |> Portfolio.Experiences.create_experience()

    experience
  end
end
