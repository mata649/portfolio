defmodule Portfolio.ProjectsTest do
  use Portfolio.DataCase

  alias Portfolio.Projects

  describe "projects" do
    alias Portfolio.Projects.Project

    import Portfolio.ProjectsFixtures
    import Portfolio.SkillsFixtures
    @invalid_attrs %{name: nil, description: nil, githubURL: nil, skills: []}
    @skills_not_loaded %Ecto.Association.NotLoaded{
      __field__: :skills,
      __cardinality__: :many,
      __owner__: Portfolio.Projects.Project
    }

    test "list_projects/0 returns all projects with skills" do
      skill = skill_fixture()
      project = project_fixture(%{skills: [skill.id]})
      assert Projects.list_projects([:skills]) == [project]
    end

    test "list_projects/0 returns all projects without skills" do
      skill = skill_fixture()
      project = project_fixture(%{skills: [skill.id]})

      assert Projects.list_projects() == [
               %Project{project | skills: @skills_not_loaded}
             ]
    end

    test "get_project!/1 returns the project with given id with skills" do
      skill = skill_fixture()
      project = project_fixture(%{skills: [skill.id]})
      assert Projects.get_project!(project.id, [:skills]) == project
    end

    test "get_project!/1 returns the project with given id without skills" do
      skill = skill_fixture()
      project = project_fixture(%{skills: [skill.id]})
      assert Projects.get_project!(project.id) == %Project{project | skills: @skills_not_loaded}
    end

    test "create_project/1 with valid data creates a project" do
      skill = skill_fixture()
      skills = [skill.id]

      valid_attrs = %{
        name: "some name",
        description: "some description",
        githubURL: "https://github.com/example/example_project",
        skills: skills
      }

      assert {:ok, %Project{} = project} = Projects.create_project(valid_attrs)
      assert project.name == "some name"
      assert project.description == "some description"
      assert project.githubURL == "https://github.com/example/example_project"
      assert project.skills == [skill]
    end

    test "create_project/1 with invalid data returns error changeset" do
      assert {:error, %Ecto.Changeset{}} = Projects.create_project(@invalid_attrs)
    end

    test "update_project/2 with valid data updates the project" do
      skill = skill_fixture()
      project = project_fixture(%{skills: [skill.id]})
      skills = [skill]

      update_attrs = %{
        name: "some updated name",
        description: "some updated description",
        githubURL: "https://github.com/example/example_project2",
        skills: [skill.id]
      }

      assert {:ok, %Project{} = project} = Projects.update_project(project, update_attrs)
      assert project.name == "some updated name"
      assert project.description == "some updated description"
      assert project.githubURL == "https://github.com/example/example_project2"
      assert project.skills == skills
    end

    test "update_project/2 with invalid data returns error changeset" do
      skill = skill_fixture()
      project = project_fixture(%{skills: [skill.id]})
      assert {:error, %Ecto.Changeset{}} = Projects.update_project(project, @invalid_attrs)
      assert project == Projects.get_project!(project.id, [:skills])
    end

    test "delete_project/1 deletes the project" do
      skill = skill_fixture()
      project = project_fixture(%{skills: [skill.id]})
      assert {:ok, %Project{}} = Projects.delete_project(project)
      assert_raise Ecto.NoResultsError, fn -> Projects.get_project!(project.id) end
    end

    test "change_project/1 returns a project changeset" do
      skill = skill_fixture()
      project = project_fixture(%{skills: [skill.id]})
      assert %Ecto.Changeset{} = Projects.change_project(project)
    end
  end
end
