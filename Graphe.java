import java.util.ArrayList;
import java.util.Scanner;

public class Graphe {

    private boolean[][] G;
    private int nbSommet;

    //Initialisation du graphe à l'appel du constructeur
    Graphe(int nbSommet) {
        G = new boolean[nbSommet][nbSommet];
        this.nbSommet = nbSommet;
        for(int i=0;i<this.nbSommet;i++){
            for(int j=0;j<this.nbSommet;j++){
                this.G[i][j] = false;
            }
        }
    }

    public boolean arc(int x, int y){
        return G[x][y];
    }

    public boolean arrete(int x, int y){
        return arc(x,y) || arc(y,x);
    }

    //Méthode de saisi d'arc, un à un
   /* public Graphe setGraphe(){
        Scanner sc = new Scanner(System.in);
        for(int i=0;i<this.nbSommet;i++){
            System.out.println("\n d'un sommet de manière boolénne : true ou false");
            for(int j=0;j<this.nbSommet;j++){
                boolean val = sc.nextBoolean();
                this.G[i][j] = val;
            }
        }
        return this;
    }*/

    //Méthode permettant de saisir un graphe au clavier (le graphe sera le résultat de la méthode)
    public Graphe setGraphe(int mode){ //mode : 1 = ajouter || mode : 2 = supprimer
        Scanner sc = new Scanner(System.in);

        //On initialise les variables utiles à la saisie des arcs dans le graphe
        int saisie1 = 0;
        int saisie2 = 0;
        int saisieFin = 0;

        while(true){
            clearScreen();
            System.out.println("Il y a "+this.nbSommet+" sommets présent dans votre graphe\nVoici la représentation ci-dessous de votre graphe à l'état actuel :");
            this.afficherGraphe();
            if(mode==0){
                System.out.println("\nSaisissez le sommet pour lequel vous voulez construire un arc (de 0 à "+(nbSommet-1)+"), saisissez -1 pour arrêter");
            }
            else{
                System.out.println("\nSaisissez le sommet pour lequel vous voulez suprimer un arc (de 0 à "+(nbSommet-1)+"), saisissez -1 pour arrêter");
            }
            saisie1 = sc.nextInt();
            if(saisie1 > this.nbSommet-1){
                System.out.println("Valeur imposible");
                continue;
            }
            else{
                if(saisie1 < 0) break; //Si saisie1= -1 on quitte le tant que
                //Si saisie1 != -1 et appartient à l'intervalle [0;nbSommet-1]
                else{
                    //même principe que saisie1
                    System.out.println("\nSaisissez maitenant le sommet à associer en tant qu'extrémité finale de l'arc " + saisie1 + "(sommetSaisie), -1 pour quitter");
                    while(true){
                        saisie2 = sc.nextInt();
                        if(saisie2 > this.nbSommet-1){
                            System.out.println("Valeur imposible");
                            continue;
                        }
                        else{
                            if(saisie2 < 0){
                                break;
                            }
                            else{
                                if(mode==0){
                                    this.G[saisie1][saisie2]=true;
                                }
                                else{
                                    this.G[saisie1][saisie2]=false;
                                }
                            }
                        }
                    }
                }
            }
        }
        clearScreen();
        System.out.println("Votre graphe a bien été saisie :");
        this.afficherGraphe();
        System.out.println("\n\nVoulez vous ajouter des arcs ? En supprimer ?\n\t-Taper 0 pour ajouter\n\t-Taper 1 pour supprimer\n\t-Taper 2 pour stoper la saisie du graphe\n\t");
        saisieFin = sc.nextInt();
        if(saisieFin == 0){
            this.setGraphe(0);
        }
        else{
            if(saisieFin == 1){
                this.setGraphe(1);
            }
            else{
                return this;
            }
        }
        return this;
    }

    //Méthode qui étant donné un graphe et un sommet x renvoie la liste des successeurs de x
    public ArrayList<Integer> succ(int x){
        ArrayList<Integer> liste = new ArrayList<Integer>();
        for(int i=0;i<this.nbSommet;i++){
            if(this.G[x][i]){
                liste.add(i);
            }
        }     
        return liste;   
    }

    //Méthode qui étant donné un graphe et un sommet x renvoie la liste des prédécesseurs de x
    public ArrayList<Integer> pred(int x){
        ArrayList<Integer> liste = new ArrayList<Integer>();
        for(int i=0;i<this.nbSommet;i++){
            if(this.G[i][x]){
                liste.add(i);
            }
        }     
        return liste;   
    }

    //Méthode qui étant donné un graphe et un sommet x renvoie l’ensemble des descendants de x
    public ArrayList<Integer> Desc(int x){
        ArrayList<Integer> explore = new ArrayList<Integer>();
        int[] num = new int[2];
        num[0]=1; num[1]=1; //int n_Emp = 1, n_Dep = 1;
        boolean luiMeme=false;
        int t_Emp[] = new int[this.nbSommet], t_Dep[] = new int[this.nbSommet];
        VisitGraphProf(x,explore,num,t_Emp,t_Dep);
        for (int i : explore) {
            if(arc(i, x)){
              luiMeme = true;
              break;
            }
        }
        if(!luiMeme) explore.remove(0);
        return explore;
    }

    //Méthode qui étant donné un graphe et un sommet x renvoie l’ensemble des ancêtres de x.
    public ArrayList<Integer> Anc(int x){
        ArrayList<Integer> ancetre = new ArrayList<Integer>();
        this.Dual();
        ancetre=Desc(x);
        this.Dual();
        return ancetre;
    }

    //Méthode qui étant donné un graphe et un sommet x calcule et renvoie la composante connexe de x.
    public ArrayList<Integer> CompCon(int x){
        ArrayList<Integer> explore = new ArrayList<Integer>();
        ArrayList<Integer> atteint = new ArrayList<Integer>();
        int i, u;
        atteint.add(x);
        while(!(atteint.isEmpty())){
            u=atteint.get(0);
            atteint.remove(0);
            explore.add(u);
            for(i=0;i<this.nbSommet;i++){
                if(!explore.contains(i) && !atteint.contains(i)){ //ne pas rajouter des sommets déjà présent
                    if(G[u][i] || G[i][u]){
                        atteint.add(i);
                    }
                }
            }
        }
        return explore;
    }

    //Méthode qui étant donné un graphe calcule et renvoie le nombre de composantes connexes du graphe.
    public int NbCompCon(){
        ArrayList<Integer> explore = new ArrayList<Integer>();
        ArrayList<Integer> temp = new ArrayList<Integer>();
        int x;
        int taille = 0;
        int nb = 0;
        
        for(x=0; x<this.nbSommet;x++){
            boolean flag=false;
            if(taille==this.nbSommet){
                break;
            }
            temp=CompCon(x);
            for (int i : temp) {
                if(!explore.contains(i)){
                    explore.add(i);
                    flag=true; //à partir de ce sommet x nous ajoutons un sommet non atteint donc = nouvelle composante connexe 
                }
            }
            taille=explore.size();
            if(flag) nb++;
        }
        return nb;
    }

    //Méthode qui transforme un graphe en un graphe non orienté et renvoie ce nouveau graphe (sans modifier le premier graphe)
    public Graphe GrapheNonOrienté(){
        Graphe NO=new Graphe(this.nbSommet);
        boolean b; int j;

        for(int i=0; i<nbSommet; i++){
            j = i+1;
            while(j<nbSommet){
                if(G[i][j] || G[j][i])
                    b=true;
                else 
                    b=false;
                NO.G[i][j]=b;
                NO.G[j][i]=b;
                j++;
            }
        }
        return NO;
    }

    //Parcours du graphe en largeur à partir du sommet x 
    public int[] ParcoursGrapheLargeur(int x){
        Graphe G2 = GrapheNonOrienté();
        ArrayList<Integer> explore=new ArrayList<Integer>();
        ArrayList<Integer> atteint=new ArrayList<Integer>();
        ArrayList<Integer> voisin=new ArrayList<Integer>();
        int u, i;

        int[] distance=new int[this.nbSommet];
        for(int h=0; h<this.nbSommet; h++){
            distance[h]=0;
        }   
        atteint.add(x);
        while(!atteint.isEmpty()){
            u = atteint.get(0); i = distance[u];
            atteint.remove(0);
            explore.add(u);//new Couple(u,i));
            voisin=G2.succ(u); //Non orienté dons succ a tous les voisins
            while(!voisin.isEmpty()){
                if(!atteint.contains(voisin.get(0)) && !explore.contains(voisin.get(0))){
                    distance[voisin.get(0)]=i+1; atteint.add(voisin.get(0));
                }
                voisin.remove(0);
            }
        }
        for(int h=0; h<this.nbSommet; h++){
            if(distance[h]==0){
                distance[h]=-1; //correspond à non atteint
            }
        }
        distance[x]=0;
        return distance;
    }

    //Parcours du graphe en profondeur à partir du sommet x 
    void ParcProf(int x, int[] t_Emp, int[] t_Dep){
        ArrayList<Integer> explore = new ArrayList<Integer>();
        int[] num = new int[2]; num[0]=1; num[1]=1;
        this.VisitGraphProf(x,explore,num,t_Emp,t_Dep); //parcous commence obligatoirement en x
        for(int h=0; h<this.nbSommet;h++){
            if(h!=x){
                this.VisitGraphProf(h,explore,num,t_Emp,t_Dep);
            }
        }
    }

    //Visite du graphe en profondeur de façon récursive (pour obtenir le tableau d'empilement de chaques sommets)
    public void VisitGraphProf(int x, ArrayList<Integer> explore, int[] num, int t_Emp[], int t_Dep[]){
        if(!explore.contains(x)){
            explore.add(x); t_Emp[x]=num[0]; num[0]++;
            ArrayList<Integer> u=this.succ(x);
            while(!(u.isEmpty())){
                this.VisitGraphProf(u.get(0), explore, num, t_Emp, t_Dep);
                u.remove(0);
            }
            t_Dep[x]=num[1]; num[1]++;
        }
    }

    //Renvoie le graphe dual du graphe utilisant cette méthode
    public void Dual(){
        Graphe D = new Graphe(this.nbSommet);
        for(int i=0;i<this.nbSommet;i++){
            for(int j=0;j<this.nbSommet;j++){
                D.G[i][j]=G[j][i];
            }
        }
        this.G=D.G;
    }

    //Méthode permettant l'affichage d'une liste
    public static void getListeEntier(ArrayList<Integer> liste){
        for(Integer l:liste){
            System.out.print(l+" ");
        }
        System.out.println();
    }

    //Méthode qui affiche le graphe
    public void afficherGraphe(){
        int i,j;
        for (i=0;i<this.nbSommet;i++){
            for (j=0;j<this.nbSommet;j++){
                if (this.G[i][j])
                    System.out.print("V ");
                else 
                    System.out.print("F ");
            }
            System.out.println("");
        }
    }

    //Méthode permettant l'affichage d'un tableau
    public void getTableauEntier(int[] tab){
        for(int i=0;i<this.nbSommet;i++){
            System.out.print(i+" ");
        }
        System.out.println();
        for(int i=0;i<this.nbSommet;i++){
            System.out.print(tab[i]+" ");
        }
        System.out.println();
    }

    //Méthode qui nettoie le terminal
    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int x, y; //sommet

        //Saisir un graphe G au clavier
        clearScreen();
        System.out.println("Ce programme vous permez de saisir un graphe\n\nVeuillez saisir le nombre de sommets qui composeront ce graphe (une valeur entière >2 est attendu)");
        int nbSommet = sc.nextInt();
        Graphe g = new Graphe(nbSommet);
        g.setGraphe(0);
        clearScreen();

        //Saisir un sommet x au clavier
        System.out.println("Il y a "+g.nbSommet+" sommets présent dans votre graphe\nVoici la représentation ci-dessous de votre graphe saisie :");
        g.afficherGraphe();
        System.out.println("\n\nVeuillez saisir un sommet x (entier compris entre 0 et "+(g.nbSommet-1)+")");
        x = sc.nextInt();

        //Calculer et afficher Succ(x) dans le graphe G ;
        System.out.println("\nLes successeurs du sommet "+x+" (x) sont :");
        getListeEntier(g.succ(x));

        //Saisir un sommet y
        System.out.println("\n\nVeuillez saisir un sommet y (entier compris entre 0 et "+(g.nbSommet-1)+")");
        y = sc.nextInt();

        //Calculer et afficher Pred(y) dans le graphe G
        System.out.println("\nLes prédécesseurs du sommet "+y+" (y) sont :");
        getListeEntier(g.pred(y));

        //Calculer et afficher Desc(y) dans le graphe G
        System.out.println("\nLes descendants du sommet "+y+" (y) sont :");
        getListeEntier(g.Desc(y));

        //Calculer et afficher Anc(x) dans le graphe G
        System.out.println("\nLes ancêtres du sommet "+x+" (x) sont :");
        getListeEntier(g.Anc(x));

        System.out.println("\nLe parcours en largeur à partir de "+x+" (x) donne le tableau de distance suivant:");
        g.getTableauEntier(g.ParcoursGrapheLargeur(x));

        System.out.println("\nLe parcours en profondeur à partir de "+x+" (x) donne :");
        int t_Emp[] = new int[g.nbSommet], t_Dep[] = new int[g.nbSommet];
        g.ParcProf(x,t_Emp,t_Dep);
        System.out.println("\nLe tableau d'empilement suivant :");
        g.getTableauEntier(t_Emp);
        System.out.println("\nLe tableau de dépilement suivant :");
        g.getTableauEntier(t_Dep);

        //Calculer et afficher CompCon(x) dans le graphe G
        System.out.println("\nLa composante connexe du sommet "+x+" (x) est :");
        getListeEntier(g.CompCon(x));

        //Calculer et afficher NbCompCon(G)
        System.out.println("\nIl y a "+g.NbCompCon()+" composantes connexes dans le graphe\n");
    }
}