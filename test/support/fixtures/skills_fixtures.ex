defmodule Portfolio.SkillsFixtures do
  @moduledoc """
  This module defines test helpers for creating
  entities via the `Portfolio.Skills` context.
  """

  @doc """
  Generate a skill.
  """
  def skill_fixture(attrs \\ %{}) do
    {:ok, skill} =
      attrs
      |> Enum.into(%{
        color: "#FFFFFF",
        name: "some name"
      })
      |> Portfolio.Skills.create_skill()

    skill
  end
end
