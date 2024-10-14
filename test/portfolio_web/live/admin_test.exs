defmodule PortfolioWeb.AdminTest do
  use PortfolioWeb.ConnCase, async: true
  import Phoenix.LiveViewTest
  import Portfolio.AccountsFixtures

  describe "Admin page" do
    test "renders admin page", %{conn: conn} do
      {:ok, _lv, html} = conn |> log_in_user(user_fixture()) |> live(~p"/admin")
      assert html =~ "Administration"
    end

    test "redirects if user not logged in", %{conn: conn} do
      {:error, redirect} = live(conn, ~p"/admin")
      assert {:redirect, %{to: path, flash: flash}} = redirect
      assert path == ~p"/"
      assert %{"error" => message} = flash
      assert message == "Nope!"
    end
  end
end
