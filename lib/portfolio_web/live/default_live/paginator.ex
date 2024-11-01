defmodule PortfolioWeb.DefaultLive.Paginator do
  use Phoenix.LiveComponent

  def render(assigns) do
    ~H"""
    <div class="flex justify-center w-full gap-3 text-xl">
      <%= if @meta.total_pages != 0 do %>
        <%= if @meta.current_page != 1 do %>
          <span
            class="transition ease-out delay-75 cursor-pointer hover:scale-110"
            phx-click="change_page"
            phx-value-page={@meta.current_page - 1}
          >
            ◄
          </span>
        <% end %>
        <%= for i <- Enum.to_list(1..@meta.total_pages) do %>
          <span
            class="transition ease-out delay-75 cursor-pointer hover:scale-110"
            style={
              if i == @meta.current_page,
                do:
                  "background-color: rgba(186, 192, 6, 0.53); box-shadow: 0px 0px 21px 5px rgba(186, 192, 6, 1);"
            }
            phx-click="change_page"
            phx-value-page={i}
          >
            <%= i %>
          </span>
        <% end %>
        <%= if @meta.current_page != @meta.total_pages do %>
          <span
            class="transition ease-out delay-75 cursor-pointer hover:scale-110"
            phx-click="change_page"
            phx-value-page={@meta.current_page + 1}
          >
            ►
          </span>
        <% end %>
      <% end %>
    </div>
    """
  end
end
