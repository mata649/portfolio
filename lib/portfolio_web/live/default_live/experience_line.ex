defmodule PortfolioWeb.DefaultLive.ExperienceLine do
  use Phoenix.LiveComponent

  def render(assigns) do
    ~H"""
    <li class="mb-10 ms-4">
      <div class="absolute w-3 h-3 bg-gray-200 rounded-full mt-1.5 -start-1.5 border border-white dark:border-gray-900 dark:bg-gray-700">
      </div>
      <time class="mb-1 text-2xl font-normal leading-none text-white dark:text-gray-500">
        <%= Calendar.strftime(@experience.from, "%B %Y") %>
      </time>
      -
      <time class="mb-1 text-2xl font-normal leading-none dark:text-gray-500">
        <%= if @experience.current_job,
          do: "Current",
          else: Calendar.strftime(@experience.to, "%B %Y") %>
      </time>
      <h3 class="text-4xl font-semibold dark:text-white">
        <%= @experience.position %>
      </h3>
      <p class="mb-4 text-2xl font-normal dark:text-gray-400">
        <%= @experience.description %>
      </p>
      <div class="flex gap-3">
        <%= for skill <- @experience.skills do %>
          <span style={get_skill_glow(skill.color)} class="p-0.5 text-2xl rounded-full ">
            <%= skill.name %>
          </span>
        <% end %>
      </div>
    </li>
    """
  end

  defp get_skill_glow(color) do
    "color: #{color};
    text-shadow: 0 0 5px #{color}, 0 0 10px #{color}, 0 0 20px #{color}, 0 0 40px #{color};"
  end
end
