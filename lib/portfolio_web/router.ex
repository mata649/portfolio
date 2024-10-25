defmodule PortfolioWeb.Router do
  use PortfolioWeb, :router
  alias PortfolioWeb

  import PortfolioWeb.UserAuth

  pipeline :browser do
    plug :accepts, ["html"]
    plug :fetch_session
    plug :fetch_live_flash
    plug :put_root_layout, html: {PortfolioWeb.Layouts, :root}
    plug :protect_from_forgery
    plug :put_secure_browser_headers
    plug :fetch_current_user
  end

  pipeline :api do
    plug :accepts, ["json"]
  end

  scope "/", PortfolioWeb do
    pipe_through :browser
    live "/", Home
  end

  # Other scopes may use custom stacks.
  # scope "/api", PortfolioWeb do
  #   pipe_through :api
  # end

  # Enable LiveDashboard and Swoosh mailbox preview in development
  if Application.compile_env(:portfolio, :dev_routes) do
    # If you want to use the LiveDashboard in production, you should put
    # it behind authentication and allow only admins to access it.
    # If your application does not have an admins-only section yet,
    # you can use Plug.BasicAuth to set up some basic authentication
    # as long as you are also using SSL (which you should anyway).
    import Phoenix.LiveDashboard.Router

    scope "/dev" do
      pipe_through :browser

      live_dashboard "/dashboard", metrics: PortfolioWeb.Telemetry
      forward "/mailbox", Plug.Swoosh.MailboxPreview
    end
  end

  ## Authentication routes

  scope "/", PortfolioWeb do
    pipe_through [:browser, :redirect_if_user_is_authenticated]

    live_session :redirect_if_user_is_authenticated,
      on_mount: [{PortfolioWeb.UserAuth, :redirect_if_user_is_authenticated}] do
      live "/users/log_in", UserLoginLive, :new
    end

    post "/users/log_in", UserSessionController, :create
  end

  scope "/", PortfolioWeb do
    pipe_through [:browser, :require_authenticated_user]

    live_session :require_authenticated_user,
      on_mount: [{PortfolioWeb.UserAuth, :ensure_authenticated}] do
      live "/admin", Admin
      # Skills
      live "/skills", SkillLive.Index, :index
      live "/skills/:id/edit", SkillLive.Index, :edit
      live "/skills/new", SkillLive.Index, :new
      live "/skills/:id", SkillLive.Show, :show
      live "/skills/:id/show/edit", SkillLive.Show, :edit

      # Projects
      live "/projects", ProjectLive.Index, :index
      live "/projects/:id/edit", ProjectLive.Index, :edit
      live "/projects/new", ProjectLive.Index, :new
      live "/projects/:id", ProjectLive.Show, :show
      live "/projects/:id/show/edit", ProjectLive.Show, :edit

      # Experiences
      live "/experiences", ExperienceLive.Index, :index
      live "/experiences/:id/edit", ExperienceLive.Index, :edit
      live "/experiences/new", ExperienceLive.Index, :new
      live "/experiences/:id", ExperienceLive.Show, :show
      live "/experiences/:id/show/edit", ExperienceLive.Show, :edit
    end
  end

  scope "/", PortfolioWeb do
    pipe_through [:browser]

    delete "/users/log_out", UserSessionController, :delete
  end
end
