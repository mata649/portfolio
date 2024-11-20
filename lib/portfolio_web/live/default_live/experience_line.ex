defmodule PortfolioWeb.DefaultLive.ExperienceLine do
  use Phoenix.LiveComponent

  def render(assigns) do
    ~H"""
    <li class="mb-10 ms-4">
      <div class="absolute w-3 h-3 bg-black rounded-full mt-1.5 -start-1.5 border border-black"></div>
      <time class="mb-1 text-xl font-normal leading-none dark:text-gray-500">
        <%= Calendar.strftime(@experience.from, "%B %Y") %>
      </time>
      -
      <time class="mb-1 text-xl font-normal leading-none">
        <%= if @experience.current_job,
          do: "Current",
          else: Calendar.strftime(@experience.to, "%B %Y") %>
      </time>
      <h3 class="text-2xl font-semibold">
        <%= @experience.position %>
      </h3>
      <h4 class="text-xl"><%= @experience.location %></h4>
      <p class="mb-4 text-xl font-normal">
        <%= @experience.description %>
      </p>
      <div class="flex gap-3">
        <%= for skill <- @experience.skills do %>
          <span class="p-0.5 text-xl rounded-full ">
            <%= skill.name %>
          </span>
        <% end %>
      </div>
    </li>
    """
  end
end
