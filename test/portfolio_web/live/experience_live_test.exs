defmodule PortfolioWeb.ExperienceLiveTest do
  use PortfolioWeb.ConnCase

  import Phoenix.LiveViewTest
  import Portfolio.ExperiencesFixtures
  import Portfolio.SkillsFixtures

  import Portfolio.AccountsFixtures

  @create_attrs %{
    position: "some position",
    description: "some description",
    location: "some location",
    to: "2024-10-20",
    from: "2024-10-20",
    company: "some company",
    current_job: false
  }

  @update_attrs %{
    position: "some updated position",
    description: "some updated description",
    location: "some updated location",
    to: "2024-10-21",
    from: "2024-10-21",
    company: "some updated company",
    current_job: false
  }
  @invalid_attrs %{
    position: nil,
    description: nil,
    location: nil,
    to: nil,
    from: nil,
    company: nil,
    current_job: false
  }

  defp create_experience(_) do
    skill = skill_fixture()
    experience = experience_fixture(%{skills: [skill.id]})
    %{experience: experience, skill: skill}
  end

  describe "Index" do
    setup [:create_experience]

    test "lists all experiences", %{conn: conn, experience: experience} do
      {:ok, _index_live, html} =
        conn |> log_in_user(user_fixture()) |> live(~p"/experiences")

      assert html =~ "Listing Experiences"
      assert html =~ experience.position
    end

    test "saves new experience", %{conn: conn, skill: skill} do
      {:ok, index_live, _html} = conn |> log_in_user(user_fixture()) |> live(~p"/experiences")

      assert index_live |> element("a", "New Experience") |> render_click() =~
               "New Experience"

      assert_patch(index_live, ~p"/experiences/new")

      assert index_live
             |> form("#experience-form", experience: @invalid_attrs)
             |> render_change() =~ "can&#39;t be blank"

      assert index_live
             |> form("#experience-form")
             |> render_submit(%{experience: Map.put(@create_attrs, :skills, [skill.id])})

      assert_patch(index_live, ~p"/experiences")

      html = render(index_live)
      assert html =~ "Experience created successfully"
      assert html =~ "some position"
    end

    test "updates experience in listing", %{conn: conn, experience: experience} do
      {:ok, index_live, _html} = conn |> log_in_user(user_fixture()) |> live(~p"/experiences")

      assert index_live |> element("#experiences-#{experience.id} a", "Edit") |> render_click() =~
               "Edit Experience"

      assert_patch(index_live, ~p"/experiences/#{experience}/edit")

      assert index_live
             |> form("#experience-form", experience: @invalid_attrs)
             |> render_change() =~ "can&#39;t be blank"

      assert index_live
             |> form("#experience-form", experience: @update_attrs)
             |> render_submit()

      assert_patch(index_live, ~p"/experiences")

      html = render(index_live)
      assert html =~ "Experience updated successfully"
      assert html =~ "some updated position"
    end

    test "deletes experience in listing", %{conn: conn, experience: experience} do
      {:ok, index_live, _html} = conn |> log_in_user(user_fixture()) |> live(~p"/experiences")

      assert index_live |> element("#experiences-#{experience.id} a", "Delete") |> render_click()
      refute has_element?(index_live, "#experiences-#{experience.id}")
    end
  end

  describe "Show" do
    setup [:create_experience]

    test "displays experience", %{conn: conn, experience: experience} do
      {:ok, _show_live, html} =
        conn |> log_in_user(user_fixture()) |> live(~p"/experiences/#{experience}")

      assert html =~ "Show Experience"
      assert html =~ experience.position
    end

    test "updates experience within modal", %{conn: conn, experience: experience} do
      {:ok, show_live, _html} =
        conn |> log_in_user(user_fixture()) |> live(~p"/experiences/#{experience}")

      assert show_live |> element("a", "Edit") |> render_click() =~
               "Edit Experience"

      assert_patch(show_live, ~p"/experiences/#{experience}/show/edit")

      assert show_live
             |> form("#experience-form", experience: @invalid_attrs)
             |> render_change() =~ "can&#39;t be blank"

      assert show_live
             |> form("#experience-form", experience: @update_attrs)
             |> render_submit()

      assert_patch(show_live, ~p"/experiences/#{experience}")

      html = render(show_live)
      assert html =~ "Experience updated successfully"
      assert html =~ "some updated position"
    end
  end
end
