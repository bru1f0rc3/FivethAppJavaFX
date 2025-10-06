package ru.demo.tradeapp.util;

import ru.demo.tradeapp.model.Product;

import java.util.HashMap;
import java.util.Map;

public class Basket
{
    protected HashMap<Product, Item> basket  = new  HashMap<Product, Item>();
    /// <summary>
    /// Value словаря - количество товара и стоимость
    /// </summary>
    public HashMap<Product, Item> getBasket()
    {
        return basket;
    }


    /// <summary>
    /// Словарь хранит товар в качестве ключа и BuyItem в качестве значения
    /// </summary>



    // очистка корзины
    public void clearBasket()
    {
        this.basket.clear();
    }
    /// <summary>
    /// Добавление товара в корзину
    /// </summary>
    /// <param name="product">Добавляемый товар</param>
    public void addProductInBasket(Product product)
    {
        // если такой товар есть в корзине
        if (this.basket.containsKey(product))
        {
            // увеличиваем его количество на +1
            int k = this.basket.get(product).count + 1;
            // пересчистваем стоимость
            double p = product.getPriceWithDiscount() * k;
            double c = product.getCost() * k;
            this.basket.put(product, new Item (product, k,c, p));
        }

        else
        {
            // добавляем новый товар в корзину в количесьве 1 шт
            double p = product.getPriceWithDiscount();
            double c = product.getCost();
            this.basket.put(product, new Item (product,1,c, p));
        }
    }
    /// <summary>
    /// Изменяет количество товара product в корзине
    /// </summary>
    /// <param name="product">Товар</param>
    /// <param name="count">количество товара</param>
    public  void setCount(Product product, int count)
    {
        if (this.basket.containsKey(product))
        {
            int k = count;
            double p = product.getPriceWithDiscount() * k;
            double c = product.getCost() * k;
            this.basket.put(product, new Item (product, k,c, p));
            // если количество 0 или меньше 0 удаляем товар из корзины
            if (k <= 0)
            {
                getBasket().remove(product);
            }
        }
    }
    /// <summary>
    /// Удаляем товар product из корзины
    /// </summary>
    /// <param name="product">Удаляемый товар</param>
    public  void deleteProductFromBasket(Product product)
    {
        if (this.basket.containsKey(product))
        {
            basket.remove(product);
        }
    }
    /// <summary>
    /// Cтоимость всех товаров в корзине
    /// </summary>
    public  double getTotalCost()
    {

        double sum = 0;
        for (Item item : basket.values())
        {
            sum += item.total;
        }
        return sum;
    }

    public  double getCostWithoutDiscont()
    {
        double sum = 0;
        for (Item item : basket.values())
        {
            sum += item.cost;
        }
        return sum;
    }

    public int getTotalDiscount()
    {
        int discount = (int) Math.round((getCostWithoutDiscont() - getTotalCost()) / getTotalCost() * 100);
        if (discount < 0)
            discount = 0;
        return discount;
    }
    /// <summary>
    /// Количество товаров в корзине
    /// </summary>
    public  int getCount()
    {
        return basket.size();

    }
    /// <summary>
    /// Возвращает true, если на складе каждого товара не меньше 3 единиц
    /// </summary>
    public boolean isOnStock()
    {
        for (Product item: basket.keySet())
        {
            if (item.getQuantityInStock() < 3)
            {
                return false;
            }
        }    return true;
    }
}

