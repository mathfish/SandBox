{{
    config(materialized="view") 
}}

with customer_orders_cte as (

    select
        customer_id,
        min(order_date) as first_order_date,
        max(order_date) as most_recent_order_date,
        count(order_id) as number_of_orders

    from 
        (
            select * from {{ ref("stg_orders") }}
        ) orders_cte

    group by 1
),

final_cte as (

    select
        customers_cte.customer_id,
        customers_cte.first_name,
        customers_cte.last_name,
        customer_orders_cte.first_order_date,
        customer_orders_cte.most_recent_order_date,
        coalesce(customer_orders_cte.number_of_orders, 0) as number_of_orders

    from 
        (
            select * from {{ ref("stg_customers") }}
        ) customers_cte

    left join customer_orders_cte using (customer_id)
)

select * from final_cte