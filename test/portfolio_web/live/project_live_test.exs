defmodule PortfolioWeb.ProjectLiveTest do
  use PortfolioWeb.ConnCase

  import Phoenix.LiveViewTest
  import Portfolio.ProjectsFixtures
  import Portfolio.AccountsFixtures
  import Portfolio.SkillsFixtures

  @create_attrs %{
    name: "some name",
    description: "some description",
    githubURL: "https://github.com/example/example_project"
  }
  @update_attrs %{
    name: "some updated name",
    description: "some updated description",
    githubURL: "https://github.com/example/example_project2"
  }
  @invalid_attrs %{name: nil, description: nil, githubURL: nil}

  defp create_project(_) do
    skill = skill_fixture()
    project = project_fixture(%{skills: [skill.id]})
    %{project: project, skill: skill}
  end

  describe "Index" do
    setup [:create_project]

    test "redirects if user is not logged in", %{conn: conn} do
      {:error, redirect} = live(conn, ~p"/projects")
      assert {:redirect, %{to: path, flash: flash}} = redirect
      assert path == ~p"/"
      assert %{"error" => message} = flash
      assert message == "Nope!"
    end

    test "lists all projects", %{conn: conn, project: project} do
      {:ok, _index_live, html} = conn |> log_in_user(user_fixture()) |> live(~p"/projects")

      assert html =~ "Listing Projects"
      assert html =~ project.name
    end

    test "saves new project", %{conn: conn, skill: skill} do
      {:ok, index_live, _html} = conn |> log_in_user(user_fixture()) |> live(~p"/projects")

      assert index_live |> element("a", "New Project") |> render_click() =~
               "New Project"

      assert_patch(index_live, ~p"/projects/new")

      assert index_live
             |> form("#project-form", project: @invalid_attrs)
             |> render_change() =~ "can&#39;t be blank"

      assert index_live
             |> form("#project-form")
             |> render_submit(%{project: Map.put(@create_attrs, :skills, [skill.id])})

      assert_patch(index_live, ~p"/projects")

      html = render(index_live)
      assert html =~ "Project created successfully"
      assert html =~ "some name"
    end

    test "updates project in listing", %{conn: conn, project: project} do
      {:ok, index_live, _html} = conn |> log_in_user(user_fixture()) |> live(~p"/projects")

      assert index_live |> element("#projects-#{project.id} a", "Edit") |> render_click() =~
               "Edit Project"

      assert_patch(index_live, ~p"/projects/#{project}/edit")

      assert index_live
             |> form("#project-form", project: @invalid_attrs)
             |> render_change() =~ "can&#39;t be blank"

      assert index_live
             |> form("#project-form", project: @update_attrs)
             |> render_submit()

      assert_patch(index_live, ~p"/projects")

      html = render(index_live)
      assert html =~ "Project updated successfully"
      assert html =~ "some updated name"
    end

    test "deletes project in listing", %{conn: conn, project: project} do
      {:ok, index_live, _html} = conn |> log_in_user(user_fixture()) |> live(~p"/projects")

      assert index_live |> element("#projects-#{project.id} a", "Delete") |> render_click()
      refute has_element?(index_live, "#projects-#{project.id}")
    end
  end

  describe "Show" do
    setup [:create_project]

    test "redirects if user is not logged in", %{conn: conn, project: project} do
      {:error, redirect} = live(conn, ~p"/projects/#{project}")
      assert {:redirect, %{to: path, flash: flash}} = redirect
      assert path == ~p"/"
      assert %{"error" => message} = flash
      assert message == "Nope!"
    end

    test "displays project when user is logged in", %{conn: conn, project: project} do
      {:ok, _show_live, html} =
        conn |> log_in_user(user_fixture()) |> live(~p"/projects/#{project}")

      assert html =~ "Show Project"
      assert html =~ project.name
    end

    test "updates project within modal", %{conn: conn, project: project} do
      {:ok, show_live, _html} =
        conn |> log_in_user(user_fixture()) |> live(~p"/projects/#{project}")

      assert show_live |> element("a", "Edit") |> render_click() =~
               "Edit Project"

      assert_patch(show_live, ~p"/projects/#{project}/show/edit")

      assert show_live
             |> form("#project-form", project: @invalid_attrs)
             |> render_change() =~ "can&#39;t be blank"

      assert show_live
             |> form("#project-form", project: @update_attrs)
             |> render_submit()

      assert_patch(show_live, ~p"/projects/#{project}")

      html = render(show_live)
      assert html =~ "Project updated successfully"
      assert html =~ "some updated name"
    end
  end
end
