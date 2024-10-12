defmodule PortfolioWeb.HomeTest do
  use PortfolioWeb.ConnCase, async: true

  import Phoenix.LiveViewTest

  describe "Home page" do
    test "renders home page", %{conn: conn} do
      {:ok, _lv, html} = live(conn, ~p"/")
      assert html =~ "Jose Mata"
    end
  end
end
