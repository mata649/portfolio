defmodule PortfolioWeb.DefaultLive.ProjectCard do
  use Phoenix.LiveComponent

  def mount(socket) do
    {:ok, socket}
  end

  def render(assigns) do
    ~H"""
    <div class="flex flex-col justify-center gap-3 mt-10 text-center bg-yellow-50">
      <h2 class="text-4xl text-black"><%= @project.name %></h2>
      <div class="flex justify-center mx-4 overflow-auto text-start ">
        <p class="w-1/2 text-xl"><%= @project.description %></p>
      </div>
      <div class="flex items-end justify-center h-10 gap-3">
        <%= for skill <- @project.skills do %>
          <span class="px-2 pt-1 mb-1 text-xl rounded-full">
            <%= skill.name %>
          </span>
        <% end %>
      </div>

      <div class="flex justify-center h-6">
        <.link href={@project.githubURL} target="_blank" class="font-bold delay-75 hover:scale-105">
          See in GitHub
        </.link>
      </div>
    </div>
    """
  end
end
