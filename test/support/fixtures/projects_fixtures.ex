defmodule Portfolio.ProjectsFixtures do
  @moduledoc """
  This module defines test helpers for creating
  entities via the `Portfolio.Projects` context.
  """

  @doc """
  Generate a project.
  """
  def project_fixture(attrs \\ %{}) do
    {:ok, project} =
      attrs
      |> Enum.into(%{
        description: "some description",
        githubURL: "https://github.com/example/example_project",
        name: "some name"
      })
      |> Portfolio.Projects.create_project()

    project
  end
end
